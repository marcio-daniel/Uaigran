import Like from "./Like";

export default interface Post{
    post_id: string;
    post_date: string;
    photoUri: string;
    description:string;
    like_list: Like[];
    comment_list: Comment[];

}