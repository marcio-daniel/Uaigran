import { useEffect, useState } from "react";
import api from "../../Services/api";
import getAuthHeader from "../../Services/auth";
import Feed from "../../components/Feed/Feed";
import MainScreen from "../../components/MainScreen/MainScreen";
import { AxiosResponse } from "axios";
import Post from "../../Types/Post";
import Like from "../../Types/Like";

export default function Home() {
  const [posts, setPosts] = useState<Post[]>([]);
  const headers = getAuthHeader();
  const user_id = localStorage.getItem("user_id");
  async function getFeed() {
    try {
      const { data } = await api.get("/post/feed", { headers });
      const posts: Post[] = data;
      setPosts(posts);
    } catch (error) {
      alert("Erro ao obter o feed , por favor recarregue a pagina!");
    }
  }
  useEffect(() => {
    getFeed();
  }, []);

  function postCreated(response: AxiosResponse) {
    if (response.status == 201) {
      getFeed();
    }
  }

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
      getFeed();
    } catch (error) {
      alert("Houve um erro ! Por favor tente novamente!");
    }
  }

  return (
    <MainScreen postCreated={postCreated}>
      <Feed posts={posts} handleLike={handleLike} />
    </MainScreen>
  );
}
