import { FormEvent, useEffect, useState } from "react";
import Text from "../Text/Text";
import { CustomInput } from "../TextInput/CustomInput";
import CustomButton from "../CustomButton/CustomButton";
import { PaperPlaneRight } from "@phosphor-icons/react";
import CommentItem from "../CommentItem/CommentItem";
import Comment from "../../Types/Comment";
import getAuthHeader from "../../Services/auth";
import api from "../../Services/api";

interface CommentFormElements extends HTMLFormControlsCollection {
  comment_text: HTMLInputElement;
}

interface CommentFormElement extends HTMLFormElement {
  readonly elements: CommentFormElements;
}

interface CommentsProps {
  postId: string;
  handleComment: (formData: FormData) => void;
}

export default function Comments({ handleComment, postId }: CommentsProps) {
  const [comments, setComents] = useState<Comment[]>();
  const headers = getAuthHeader();

  async function getComments() {
    try {
      const { data } = await api.get(`/comment/list/${postId}`, { headers });
      const comments: Comment[] = data;
      setComents(comments);
    } catch (error) {
      alert("Houve um erro ao tentar carregar os comentários do post!");
    }
  }

  useEffect(() => {
    getComments();
  }, []);

  async function handleSubmit(event: FormEvent<CommentFormElement>) {
    event.preventDefault();
    const form = event.currentTarget;
    const user_id = localStorage.getItem("user_id");

    const formData = new FormData();
    formData.append("user_id", user_id + "");
    formData.append("post_id", postId + "");
    formData.append("comment_text", form.elements.comment_text.value);
    await handleComment(formData);
    form.elements.comment_text.value = "";
    getComments();
  }

  return (
    <div>
      <Text size="md" color="light-gray" className="font-bold ml-2">
        Comentarios
      </Text>
      <form onSubmit={handleSubmit} className="border-b border-slate-400">
        <div className="py-4 mb-2 ml-2">
          <CustomInput.Root className="w-[700px] h-[40px] mt-2">
            <CustomInput.Input
              id="comment_text"
              type="text"
              placeholder="Comente algo!"
            />
            <CustomButton type="submit" className="bg-transparent">
              <PaperPlaneRight size={25} color="#7C7C8A" weight="light" />
            </CustomButton>
          </CustomInput.Root>
        </div>
      </form>
      <section>
        {comments &&
          comments.map((comment: Comment) => {
            return <CommentItem comment={comment} key={comment.comment_id} />;
          })}
      </section>
    </div>
  );
}
