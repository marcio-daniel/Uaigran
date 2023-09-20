import User from "./User";

export default interface Comment{
    comment_id: string;
    comment_text: string;
    post_id: string;
    user:User;
}