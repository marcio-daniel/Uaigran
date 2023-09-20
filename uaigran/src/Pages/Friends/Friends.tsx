import { useEffect, useState } from "react";
import MainScreen from "../../components/MainScreen/MainScreen";
import Text from "../../components/Text/Text";
import api from "../../Services/api";
import getAuthHeader from "../../Services/auth";
import { UserCircle } from "@phosphor-icons/react";
import CustomButton from "../../components/CustomButton/CustomButton";
import Friend from "../../Types/Friend";
import { Link } from "react-router-dom";

export default function Friends() {
  const [friends, setFriends] = useState<Friend[]>();
  const user_id = localStorage.getItem("user_id");
  const [refresh, setRefresh] = useState(false);
  const headers = getAuthHeader();
  useEffect(() => {
    async function getProfiles() {
      try {
        const { data } = await api.get(`/friend/list/${user_id}`, { headers });
        const profiles: Friend[] = data;
        setFriends(profiles);
        console.log(profiles);
      } catch (error) {
        alert(
          "Houve um erro, por favor recarregue a pagina e tente novamente!"
        );
      }
    }
    getProfiles();
  }, [refresh]);

  async function handleDeleteFriend(friend: Friend) {
    try {
      const request = {
        owner_id: localStorage.getItem("user_id"),
        friend_id: friend.friend_id,
      };
      await api.delete("/friend/delete", { data: request, headers });
      refresh ? setRefresh(false) : setRefresh(true);
    } catch (error) {
      alert("Houve um erro! Por favor tente novamente!");
    }
  }

  return (
    <MainScreen>
      <div className="basis-5/6 overflow-y-auto scroll-smooth">
        <div className="border-b border-slate-400 mt-4 pb-2 flex flex-row justify-between items-center">
          <div className="ml-2">
            <Text size="md" color="light-gray" className="font-extrabold ">
              Amigos
            </Text>
          </div>
          <div className="flex items-center mr-4">
            <Link to={"/addFriend"}>
              <CustomButton type="button" className="py-1 mb-2 mt-2 px-2">
                Adicionar Amigos
              </CustomButton>
            </Link>
          </div>
        </div>
        {friends &&
          friends.map((friend) => {
            return (
              <div
                className="my-4 border-b border-slate-400"
                key={friend.friend_id}
              >
                <div className="ml-4">
                  <div className="flex flex-col items-start ml-2 justify-center ">
                    <UserCircle size={48} color="#E1E1E1" />
                    <Text color="light-gray" size="md">
                      {friend.name}
                    </Text>
                    <CustomButton
                      type="button"
                      onClick={() => {
                        handleDeleteFriend(friend);
                      }}
                      className="py-1 mb-2 mt-2 px-2"
                    >
                      Excluir amizade
                    </CustomButton>
                  </div>
                </div>
              </div>
            );
          })}
      </div>
    </MainScreen>
  );
}
