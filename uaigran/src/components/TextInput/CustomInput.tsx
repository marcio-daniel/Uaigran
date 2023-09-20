import clsx from "clsx";
import {InputHTMLAttributes,TextareaHTMLAttributes, ReactNode} from "react";


interface CustomInputRootProps{
    children: ReactNode;
    className?: string;
}
function CustomInputRoot(props: CustomInputRootProps) {
    return (
        <div className={clsx(
            "flex items-center",
            "rounded bg-[#202024] focus-within:ring-2 ring-[#E1E1E1]",
            "py-4 px-3 ",
            {
                "gap-3 h-12 " : !props.className
            },
            props.className
        )}>
            {props.children}
        </div>
    )
}
interface CustomInputIconProps{
    children: ReactNode;
}
function CustomInputIcon(props: CustomInputIconProps) {
    return (
        <span className="w-6 h-6 text-gray-400 flex flex-col items-center justify-center">
            {props.children}
        </span>
    )
}
interface CustomInputInputProps extends InputHTMLAttributes<HTMLInputElement>{


}
interface CustomInputTextAreaProps extends TextareaHTMLAttributes<HTMLTextAreaElement>{
    

}
function CustomInputInput(props: CustomInputInputProps) {
    return (
        <input className="bg-transparent flex-1 text-[#7C7C8A] text-xs outline-none placeholder:text-gray-400"
        {...props}
        />
    )
}
function CustomInputTextArea(props: CustomInputTextAreaProps) {
    return (
        <textarea className="bg-transparent flex-1 text-[#7C7C8A] text-xs outline-none placeholder:text-gray-400 resize-none"
        {...props}
        />
    )
}

export const CustomInput = {
    Root: CustomInputRoot,
    Icon: CustomInputIcon,
    Input: CustomInputInput,
    TextArea: CustomInputTextArea
}