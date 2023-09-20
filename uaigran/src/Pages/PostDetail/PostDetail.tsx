import { useEffect, useState} from "react";
import MainScreen from "../../components/MainScreen/MainScreen";
import getAuthHeader from "../../Services/auth";
import api from "../../Services/api";
import { useParams } from "react-router-dom";
import Post from "../../Types/Post";
import PostItem from "../../components/PostItem/PostItem";
import Like from "../../Types/Like";
import Comments from "../../components/Comments/Comments";


export default function PostDetail() {
  const [post, setPost] = useState<Post>();
  const user_id = localStorage.getItem("user_id");
  const { postId } = useParams();

  const headers = getAuthHeader();

  async function getPost() {
    try {
      const { data } = await api.get(`/post/${postId}`, { headers });
      const p: Post = data;
      setPost(p);
    } catch (error) {
      alert(
        "Erro ao obter os detalhes do post , por favor recarregue a pagina!"
      );
    }
  }

  useEffect(() => {
    getPost();
  }, []);

  async function handleLike(post: Post) {
    try {
      const { data } = await api.get(`/like/${post.post_id}/${user_id}`, {
        headers,
      });
      const like: Like = data;
      if (!like) {
        const like_request = {
          post_id: post.post_id,
          user_id: user_id + "",
        };
        const { data } = await api.post("/like/create", like_request, {
          headers,
        });
      } else {
        const response = await api.delete(`/like/delete/${like.like_id}`, {
          headers,
        });
      }
      getPost();
    } catch (error) {
      alert("Houve um erro ! Por favor tente novamente!");
    }
  }

  async function handleComment(formData: FormData) {
    try {
      const response = await api.post("/comment/create", formData, { headers });
      getPost();
    } catch (error) {
      console.log(error);
      alert("Houve um erro !");
    }
  }


  return (
    <MainScreen>
      <div className="basis-5/6 overflow-y-auto scroll-smooth">
        {post && <PostItem post={post} handleLike={handleLike} />}
        {post && <Comments postId={postId+""} handleComment={handleComment} />}
      </div>
    </MainScreen>
  );
}
