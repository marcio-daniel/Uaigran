import { useNavigate } from "react-router-dom";
import { FormEvent,useState } from "react";
import CustomButton from "../CustomButton/CustomButton";
import * as Dialog from "@radix-ui/react-dialog";
import { CustomInput } from "../TextInput/CustomInput";
import Text from "../Text/Text";
import { AxiosResponse } from "axios";
import { X } from "@phosphor-icons/react";
import api from "../../Services/api";
import getAuthHeader from "../../Services/auth";
import { Dropzone } from "../Dropzone/Dropzone";

interface PostFormElements extends HTMLFormControlsCollection {
  description: HTMLTextAreaElement;
}

interface PostFormElement extends HTMLFormElement {
  readonly elements: PostFormElements;
}

interface MenuFooterProps {
  postCreated?: (response: AxiosResponse) => void;
}

export default function MenuFooter(props: MenuFooterProps) {
  const [open, setOpen] = useState(false);
  const [selectedFile,setSelectedFile] = useState<File>();
  const headers = getAuthHeader();
  const navigation = useNavigate();

  function postCreated(response: AxiosResponse) {
    setOpen(false);
    props.postCreated && props.postCreated(response);
  }

  function handleLogout() {
    localStorage.clear();
    navigation("/");
  }

  async function handleCreatePost(data: FormData) {
    try {
      const response = await api.post("/post/create", data, { headers });
      postCreated(response);
    } catch (error) {
        console.log(error)
      alert("Houve um erro , por favor tente novamente!");
    }
  }

  function handleSubmit(event: FormEvent<PostFormElement>) {
    event.preventDefault();
    const form = event.currentTarget;
    const user_id = localStorage.getItem("user_id");

    const formData = new FormData();
    formData.append("description",form.elements.description.value);
    formData.append("post_owner",user_id+"");
    if(selectedFile){
        formData.append("photo",selectedFile);
    }
    handleCreatePost(formData);
  }
  return (
    <footer className="flex flex-col items-center w-52">
      <Dialog.Root open={open} onOpenChange={setOpen}>
        <Dialog.Trigger className="w-full py-2 items-center mr-4 mt-3 bg-cyan-500 rounded font-semibold text-black text-sm transition-colors hover:bg-cyan-300">
          Novo Post
        </Dialog.Trigger>
        <Dialog.Portal>
          <Dialog.Overlay className="bg-black/60 inset-0 fixed" />

          <Dialog.Content className="fixed bg-[#121214] py-2 px-10 text-white top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 rounded-lg w-96 shadow-lg shadow-black">
            <div className="flex flex-row justify-between items-center">
              <div className="flex items-start justify-center mt-2">
                <Dialog.Title className="text-2xl font-extrabold">
                  Novo Post
                </Dialog.Title>
              </div>
              <div className="mt-1 flex items-end justify-center">
                <Dialog.Close
                  type="button"
                  className="bg-transparent px-5 h-12 rounded-md hover:bg-zinc-600"
                >
                  <X />
                </Dialog.Close>
              </div>
            </div>
            <form onSubmit={handleSubmit} className="flex flex-col gap-2 pt-1">
              <Text color="light-gray" size="sm">
                Descrição
              </Text>
              <CustomInput.Root className="w-[305px]">
                <CustomInput.TextArea
                  cols={20}
                  rows={7}
                  id="description"
                  placeholder="Digite aqui sua descrição!"
                />
              </CustomInput.Root>
              <Text color="light-gray" size="sm">
                Foto
              </Text>
              <Dropzone onFileUploaded={setSelectedFile} />
              <div className="mt-4 flex justify-end gap-4">
                <CustomButton className="flex-none px-6 py-3" type="submit">
                  Postar
                </CustomButton>
              </div>
            </form>
          </Dialog.Content>
        </Dialog.Portal>
      </Dialog.Root>

      <CustomButton
        type="button"
        onClick={handleLogout}
        className="w-full py-2 mr-4 mt-3 transition-colors hover:bg-sky-300 font-semibold"
      >
        Sair
      </CustomButton>
    </footer>
  );
}
