import AuthFormElements from "./AuthFormElements";

export default interface AuthFormElement extends HTMLFormElement{
    readonly elements : AuthFormElements;
}