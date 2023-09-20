import { Chat, Heart } from "@phosphor-icons/react";
import Post from "../../Types/Post";
import Text from "../Text/Text";
import { useEffect, useState } from "react";
import api from "../../Services/api";
import getAuthHeader from "../../Services/auth";
import { useNavigate } from "react-router-dom";

interface PostItemProps {
  post: Post;
  handleLike: (post: Post) => void;
}
export default function PostItem({ post, handleLike }: PostItemProps) {
  const [like, setLike] = useState(false);
  const user_id = localStorage.getItem("user_id");
  const headers = getAuthHeader();
  const navigate = useNavigate();

  useEffect(() => {
    async function getLike() {
      try {
        const { data } = await api.get(`/like/${post.post_id}/${user_id}`, {
          headers,
        });

        if (data) {
          setLike(true);
        }
      } catch (error) {
        alert("Houve um erro!");
      }
    }
    getLike();
  }, []);

  function changeLike(post: Post) {
    like ? setLike(false) : setLike(true);
    handleLike(post);
  }

  return (
    <div className="my-4 border-b border-slate-400">
      <div className="ml-10">
        {post.photoUri && (
          <img src={post.photoUri} className="max-w-sm max-h-sm rounded-lg " />
        )}
        <Text size="sm" color="light-gray" className="font-bold">
          <p className="mt-4">{post.description}</p>
        </Text>
        <footer className="flex items-center my-4 space-x-2">
          <div
            className="hover:bg-sky-400 rounded-full p-1"
            onClick={() => {
              navigate(`/postDetail/${post.post_id}`);
            }}
          >
            <Chat size={30} color="#E1E1E1" />
          </div>
          <Text size="sm" color="light-gray">
            {post.comment_list.length}
          </Text>
          <div
            className="hover:bg-sky-400 rounded-full p-1"
            onClick={() => changeLike(post)}
          >
            {like ? (
              <Heart size={30} className="text-red-500" weight="fill" />
            ) : (
              <Heart size={30} color="#E1E1E1" weight="light" />
            )}
          </div>
          <Text size="sm" color="light-gray">
            {post.like_list.length}
          </Text>
        </footer>
      </div>
    </div>
  );
}
