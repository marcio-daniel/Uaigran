import { ReactNode } from "react";

interface MenuItemRootProps {
  children: ReactNode;
}
function MenuItemRoot(props: MenuItemRootProps) {
  return (
    <li className="mt-5">
      <div className="flex items-center px-4 rounded-full hover:bg-sky-400">
        {props.children}
      </div>
    </li>
  );
}

interface MenuItemIconProps {
  children: ReactNode;
}

function MenuItemIcon(props: MenuItemIconProps) {
  return <div>{props.children}</div>;
}

export const MenuItem = {
  Root: MenuItemRoot,
  Icon: MenuItemIcon,
};
