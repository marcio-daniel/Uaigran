import Text from "../Text/Text";
import Comment from "../../Types/Comment";

interface CommentItemProps {
  comment: Comment;
}
export default function CommentItem({ comment }: CommentItemProps) {
  return (
    <div className="py-4 mt-1 border-b border-slate-400">
      <div className="flex flex-col ml-3">
        <div className="">
          <Text color="light-gray" className="font-regular text-[12px]">
            {comment.user.name}
          </Text>
        </div>
        <Text size="md" color="light-gray" className="font-bold ml-3">
          <p className="pt-2">{comment.comment_text}</p>
        </Text>
      </div>
    </div>
  );
}
