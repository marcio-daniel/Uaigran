import {ReactNode} from 'react';
import Menu from "../Menu/Menu";
import { AxiosResponse } from 'axios';

interface MainScreenProps{
  children: ReactNode;
  postCreated?: (response: AxiosResponse)=>void;
}


export default function MainScreen(props : MainScreenProps) {
  return (
    <div className="w-screen h-screen flex">
        <Menu postCreated={props.postCreated}/>
        {props.children}
    </div>
  );
}
