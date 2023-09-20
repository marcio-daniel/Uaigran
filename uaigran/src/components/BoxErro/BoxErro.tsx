import clsx from "clsx";
import { ReactNode } from "react";

export interface BoxErroProps{
    children: ReactNode;
    className?: string;
}

export default function BoxErro(props: BoxErroProps){
    return (
        <div className={clsx(
            props.className
        )}>
            {props.children}
        </div>

    );
}