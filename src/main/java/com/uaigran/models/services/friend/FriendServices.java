package com.uaigran.models.services.friend;

import com.uaigran.exceptions.BadRequestException;
import com.uaigran.exceptions.ConflictException;
import com.uaigran.exceptions.InternalServerException;
import com.uaigran.models.entities.Friend;
import com.uaigran.models.entities.FriendPrimaryKey;
import com.uaigran.models.entities.User.User;
import com.uaigran.models.repository.IFriendRepository;
import com.uaigran.models.services.user.IUserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FriendServices implements IFriendServices
{

    @Autowired
    private IFriendRepository _friendRepository;

    @Autowired
    private IUserServices _userServices;



    public ResponseEntity<Object> addFriend(FriendRequest request) throws BadRequestException, InternalServerException, ConflictException {
        try {
            var owner = _userServices.getUserById(request.owner_id);
            var friend = _userServices.getUserById(request.friend_id);
            if(owner == null || friend == null) {
                throw new BadRequestException("No user with these credentials was found!");
            }
            var newFriend = Friend
                    .builder()
                    .owner(owner)
                    .friend(friend)
                    .id(FriendPrimaryKey
                            .builder()
                            .owner(owner.getId())
                            .friend(friend.getId())
                            .build()
                        )
                    .build();
            if(_friendRepository.findById(newFriend.getId()).isPresent()){
                throw new ConflictException("The user already has this friendship!");
            }
            _friendRepository.save(newFriend);
            return ResponseEntity.ok().body("New friend successfully added!");
        } catch (BadRequestException e){
            throw new BadRequestException(e.getMessage());
        }
        catch (ConflictException e){
            throw new ConflictException(e.getMessage());
        }
        catch (Exception e){
            throw new InternalServerException(e.getMessage());
        }
    }

    public ResponseEntity<Object> removeFriend(FriendRequest request) throws BadRequestException, InternalServerException {
        try{
            var owner = _userServices.getUserById(request.owner_id);
            var friend = _userServices.getUserById(request.friend_id);
            if(owner == null || friend == null) {
                throw new BadRequestException("No user with these credentials was found!");
            }
            var oldFriend = Friend
                    .builder()
                    .owner(owner)
                    .friend(friend)
                    .id(FriendPrimaryKey
                            .builder()
                            .owner(owner.getId())
                            .friend(friend.getId())
                            .build()
                    )
                    .build();
            if(_friendRepository.findById(oldFriend.getId()).isEmpty()){
                throw new BadRequestException("There is no friendship with this user!");
            }
            _friendRepository.delete(oldFriend);
            return ResponseEntity.ok().body("Friendship successfully removed!");
        } catch (BadRequestException e){
            throw new BadRequestException(e.getMessage());
        }
        catch (Exception e){
            throw new InternalServerException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Object> profiles() throws InternalServerException, BadRequestException {
        try {
            var user_id = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            var user = _userServices.getUserById(user_id);
            if(user == null){
                throw new BadRequestException("No user with these credentials was found!");
            }
            var response = new ArrayList<FriendResponse>();
            var users = _userServices.list();
            users.remove(user);
            for(Friend f: user.getFriends()){
                users.remove(f.getFriend());
            }
            for (User u : users) {

                if(!user.getFriends().contains(u)){
                    response.add(
                            FriendResponse
                                    .builder()
                                    .friend_id(u.getId())
                                    .name(u.getName())
                                    .photoUri(u.getPhotoUri())
                                    .build()
                    );
                }
            }
            return ResponseEntity.ok().body(response);
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e){
            throw new InternalServerException(e.getMessage());
        }
    }

    public ResponseEntity<Object> findAllFriendsByOwner(UUID owner_id) throws BadRequestException, InternalServerException {
        try{
            var user = _userServices.getUserById(owner_id);
            if(user == null){
                throw new BadRequestException("No user with these credentials was found!");
            }
            var friends_list = _friendRepository.findByOwner(user);
            List<FriendResponse> response = new ArrayList<>();

            for(Friend f : friends_list){

                var friend = FriendResponse
                        .builder()
                        .name(f.getFriend().getName())
                        .friend_id(f.getFriend().getId())
                        .photoUri(f.getFriend().getPhotoUri())
                        .build();

                response.add(friend);
            }

            return ResponseEntity.ok().body(response);

        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e){
            throw new InternalServerException(e.getMessage());
        }

    }

}
