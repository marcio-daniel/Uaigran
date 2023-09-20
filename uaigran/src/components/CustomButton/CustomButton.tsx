import clsx from "clsx";
import { ReactNode, ButtonHTMLAttributes } from "react";

export interface CustomButtonProps
  extends ButtonHTMLAttributes<HTMLButtonElement> {
  children: ReactNode;
  className?: string;
}

export default function CustomButton(props: CustomButtonProps) {
  return (
    <button
      {...props}
      className={clsx(
        props.className,
        "bg-cyan-500",
        "rounded",
        "font-semibold",
        "text-black",
        "text-sm",
        "transition-colors hover:bg-cyan-300"
      )}
    >
      {props.children}
    </button>
  );
}
