import Post from "../../Types/Post";
import PostItem from "../PostItem/PostItem";

interface PostsProps {
  posts: Post[];
  handleLike: (post:Post)=>void;
}

export default function Posts(props: PostsProps) {
  return (
    <section>
      {props.posts &&
        props.posts.map((post: Post) => {
          return <PostItem post={post} key={post.post_id} handleLike={props.handleLike}/>;
        })}
    </section>
  );
}
