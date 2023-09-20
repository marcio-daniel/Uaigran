import { useEffect, useState } from "react";
import MainScreen from "../../components/MainScreen/MainScreen";
import Posts from "../../components/Posts/Posts";
import Text from "../../components/Text/Text";
import getAuthHeader from "../../Services/auth";
import api from "../../Services/api";
import User from "../../Types/User";
import { UserCircle } from "@phosphor-icons/react";
import { Link } from "react-router-dom";
import CustomButton from "../../components/CustomButton/CustomButton";
import Post from "../../Types/Post";
import Like from "../../Types/Like";

export default function Profile() {
  const [posts, setPosts] = useState([]);
  const [user, setUser] = useState<User>();
  const user_id = localStorage.getItem("user_id");

  const headers = getAuthHeader();
  useEffect(() => {
    async function getProfile() {
      try {
        const { data } = await api.get(`/user/profile/${user_id}`, { headers });
        const profile: User = data;
        setUser(profile);
      } catch (error) {
        alert(
          "Houve um erro, por favor recarregue a pagina e tente novamente!"
        );
      }
    }
    getProfile();
  }, []);

  async function getPosts() {
    try {
      const { data } = await api.get(`/post/list/${user_id}`, { headers });
      setPosts(data);
    } catch (error) {
      alert("Erro ao obter o feed , por favor recarregue a pagina!");
    }
  }

  useEffect(() => {
    getPosts();
  }, []);

  async function handleLike(post: Post) {
    try {
      const { data } = await api.get(`/like/${post.post_id}/${user_id}`, { headers });
      const like : Like = data;
      if (!like) {
        const like_request = {
          post_id: post.post_id,
          user_id: user_id+"",
        };
        const { data } = await api.post("/like/create", like_request, {
          headers,
        });
      }else{
        const response = await api.delete(`/like/delete/${like.like_id}`,{headers});
      }
      getPosts();
    } catch (error) {
      alert("Houve um erro ! Por favor tente novamente!");
    }
  }

  return (
    <MainScreen>
      <div className="basis-5/6 overflow-y-auto scroll-smooth">
        <div className="border-b border-slate-400 mt-4 pb-2 flex flex-row justify-between items-center">
          <div className="ml-2">
            <Text size="md" color="light-gray" className="font-extrabold ">
              Perfil
            </Text>
          </div>
          <div className="flex items-center mr-4">
            <Link to={"/editProfile"}>
              <CustomButton type="button" className="py-1 mb-2 mt-2 px-2">
                Editar Perfil
              </CustomButton>
            </Link>
          </div>
        </div>
        <div className="flex flex-col mt-6 border-b border-slate-400 pb-6">
          <div className="flex flex-col items-center justify-center">
            {user?.photoUri == null ? (
              <UserCircle size={250} color="#E1E1E1" />
            ) : (
              <img
                src={user.photoUri}
                className="rounded-[120px]  max-w-md max-h-md"
              />
            )}
          </div>
          <div className="ml-12 flex flex-col">
            <Text color="light-gray" size="lg" className="mt-4 font-extrabold">
              {user?.name}
            </Text>
            <Text color="gray" size="sm" className="font-regular pt-2">
              {user?.description}
            </Text>
          </div>
        </div>
        <div className=" mt-4 pb-2">
          <Text size="sm" color="light-gray" className="ml-2 font-extrabold ">
            Suas Postagens
          </Text>
          {posts.length == 0 ? (
            <div className="mt-10 flex flex-col items-center justify-center">
              <Text color="light-gray" size="lg" className="mb-2">
                Você ainda não fez nenhuma postagem!
              </Text>
              <Text color="light-gray" size="lg">
                Clique no botão ao lado e compartilhe algo!
              </Text>
            </div>
          ) : (
            <Posts posts={posts} handleLike={handleLike}/>
          )}
        </div>
      </div>
    </MainScreen>
  );
}
