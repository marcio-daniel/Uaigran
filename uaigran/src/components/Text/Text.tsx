import {ReactNode} from 'react';
import { clsx } from 'clsx';


export interface TextProps{
    size?: "sm" | "md" | "lg";
    color?: "gray" | "white" | "light-gray";
    children: ReactNode;
    className?: string;
}

export default function Text(props: TextProps) {

    return (
        <span className={clsx(
            {
                "text-[#E1E1E1]": props.color === "light-gray",
                "text-[#7C7C8A]": props.color === "gray",
                "text-[#FFFFFF]": props.color === "white"
            },
            {
                "text-xl" : props.size === "sm",
                "text-2xl" : props.size === "md",
                "text-3xl" : props.size === "lg",
            },
            "font-sans",
            props.className
        )} >
            {props.children}
        </span>
        
    )
}
