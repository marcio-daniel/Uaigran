import Post from "../../Types/Post";
import Posts from "../Posts/Posts";
import Text from "../Text/Text";

interface FeedProps {
  posts: Post[];
  handleLike: (post:Post)=>void;
}

export default function Feed(props: FeedProps) {

  return (
    <div className="basis-5/6 overflow-y-auto scroll-smooth">
      <div className="border-b border-slate-400 mt-4 pb-2">
        <Text size="md" color="light-gray" className="ml-2 font-extrabold ">
          Pagina Inicial
        </Text>
      </div>
      <Posts posts={props.posts} handleLike={props.handleLike}/>
    </div>
  );
}
