import { useEffect, useState,FormEvent } from "react";
import MainScreen from "../../components/MainScreen/MainScreen";
import Text from "../../components/Text/Text";
import getAuthHeader from "../../Services/auth";
import api from "../../Services/api";
import User from "../../Types/User";
import CustomButton from "../../components/CustomButton/CustomButton";
import { CustomInput } from "../../components/TextInput/CustomInput";
import { Dropzone } from "../../components/Dropzone/Dropzone";
import { useNavigate } from "react-router-dom";

interface EditProfileFormElements extends HTMLFormControlsCollection {
    name:HTMLInputElement;
    description: HTMLTextAreaElement;
  }
  
  interface EditProfileFormElement extends HTMLFormElement {
    readonly elements: EditProfileFormElements;
  }

export default function EditProfile() {
  const [profile, setProfile] = useState<User>();
  const [name, setName] = useState<string | undefined>("");
  const [description,setDescription] = useState<string | undefined>("");
  const user_id = localStorage.getItem("user_id");
  const [selectedFile, setSelectedFile] = useState<File>();
  const navigation = useNavigate();
  const headers = getAuthHeader();

  useEffect(() => {
    async function getProfile() {
      try {
        const { data } = await api.get(`/user/profile/${user_id}`, { headers });
        const profile: User = data;
        setProfile(profile);
        setName(profile.name);
        setDescription(profile.description)
      } catch (error) {
        alert(
          "Houve um erro, por favor recarregue a pagina e tente novamente!"
        );
      }
    }
    getProfile();
  }, []);

  async function handleEditProfile(formData : FormData){
    try {
        const response = await api.post("/user/profile/update", formData, { headers });
        navigation("/profile");
      } catch (error) {
          console.log(error)
        alert("Houve um erro , por favor tente novamente!");
      }
  }

  function handleSubmit(event: FormEvent<EditProfileFormElement>) {
    event.preventDefault();
    const form = event.currentTarget;
    const user_id = localStorage.getItem("user_id");

    const formData = new FormData();
    formData.append("description",form.elements.description.value);
    formData.append("id",user_id+"");
    if(selectedFile){
        formData.append("photo",selectedFile);
    }
    formData.append("name",form.elements.name.value);
    handleEditProfile(formData);
  }

  return (
    <MainScreen>
      <div className="basis-5/6 overflow-y-auto scroll-smooth">
        <div className="border-b border-slate-400 mt-4 pb-2 flex flex-row justify-between items-center">
          <div className="ml-2">
            <Text size="md" color="light-gray" className="font-extrabold ">
              Novo Perfil
            </Text>
          </div>
        </div>
        <div className=" mt-2 pb-2 ml-8">
          <form onSubmit={handleSubmit} className="flex flex-col gap-2 py-2">
            <Text size="sm" color="light-gray">
              Nome
            </Text>
            <CustomInput.Root className="max-w-sm">
              <CustomInput.Input
                placeholder="Digite seu Nome"
                type="text"
                value={name}
                onChange={(name) => {
                  setName(name.currentTarget.value);
                }}
                required
                id="name"
              />
            </CustomInput.Root>
            <Text color="light-gray" size="lg" className="mt-2 font-bold">
              Foto de perfil
            </Text>
            <Dropzone onFileUploaded={setSelectedFile} className="h-[250px]" />
            <Text color="light-gray" size="lg" className="mt-2 font-bold">
              Descrição
            </Text>
            <CustomInput.Root className="max-w-lg">
              <CustomInput.TextArea
                rows={7}
                id="description"
                value={description}
                onChange={
                  (description) => {
                    setDescription(description.currentTarget.value);
                  }
                }
                placeholder="Adicione sua descrição!"
              />
            </CustomInput.Root>
            <div className="max-w-lg flex justify-center">
              <CustomButton type="submit" className="py-2 mb-2 mt-2 px-2">
                Salvar
              </CustomButton>
            </div>
          </form>
        </div>
      </div>
    </MainScreen>
  );
}
