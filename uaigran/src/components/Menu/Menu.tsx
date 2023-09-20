import { Link } from "react-router-dom";
import Text from "../Text/Text";
import { House, User, UsersThree } from "@phosphor-icons/react";
import { MenuItem } from "../MenuItem/MenuItem";
import { AxiosResponse } from "axios";
import MenuFooter from "../MenuFooter/MenuFooter";

interface MenuProps {
  postCreated?: (response: AxiosResponse) => void;
}

export default function Menu(props: MenuProps) {
  return (
    <div className="basis-1/6 border-r border-slate-400 ml-4">
      <div className="flex  flex-col items-center justify-center">
        <Text size="lg" className="font-extrabold mt-4" color="light-gray">
          Uaigran
        </Text>
        <ul className="pr-2">
          <Link to={"/home"}>
            <MenuItem.Root>
              <MenuItem.Icon>
                <House size={35} weight="fill" color="#E1E1E1" />
              </MenuItem.Icon>
              <Text
                className="font-extrabold pl-1"
                size="sm"
                color="light-gray"
              >
                Pagina inicial
              </Text>
            </MenuItem.Root>
          </Link>
          <Link to={"/profile"}>
            <MenuItem.Root>
              <MenuItem.Icon>
                <User size={35} weight="fill" color="#E1E1E1" />
              </MenuItem.Icon>
              <Text
                className="font-extrabold pl-1"
                size="sm"
                color="light-gray"
              >
                Perfil
              </Text>
            </MenuItem.Root>
          </Link>
          <Link to={"/friends"}>
            <MenuItem.Root>
              <MenuItem.Icon>
                <UsersThree size={35} weight="fill" color="#E1E1E1" />
              </MenuItem.Icon>
              <Text
                className="font-extrabold pl-1"
                size="sm"
                color="light-gray"
              >
                Amigos
              </Text>
            </MenuItem.Root>
          </Link>
        </ul>
        <MenuFooter postCreated={props.postCreated} />
      </div>
    </div>
  );
}
