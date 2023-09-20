import { Envelope, Lock, User } from "@phosphor-icons/react";
import { FormEvent, useState } from "react";
import { Link, useNavigate } from "react-router-dom";

import { CustomInput } from "../../components/TextInput/CustomInput";
import CustomButton from "../../components/CustomButton/CustomButton";
import Text from "../../components/Text/Text";
import Auth from "../../Types/Auth";
import api from "../../Services/api";
import AuthFormElement from "../../Types/FormTypes/AuthFormElement";
import BoxErro from "../../components/BoxErro/BoxErro";

export default function Registration() {
  const [model, setModel] = useState(false);

  const navigate = useNavigate();

  async function handleRegister(auth: Auth) {
    try {
      const response = await api.post("/user/create", auth);
      console.log(response);
      navigate("/");
    } catch (error) {
      if (error.response.status == 409) {
        setModel(true);
      }
    }
  }

  function handleSubimit(events: FormEvent<AuthFormElement>) {
    events.preventDefault();
    if (model) {
      setModel(false);
    }
    const form = events.currentTarget;
    const auth: Auth = {
      name: form.elements.name.value,
      email: form.elements.email.value,
      password: form.elements.password.value,
      role: "USER",
    };
    if (auth.name === "") {
      delete auth.name;
    }
    handleRegister(auth);
  }

  return (
    <div className="flex flex-col items-center mt-10">
      <header className="flex flex-col items items-center mb-4">
        <Text size="lg" color="light-gray" className="mt-1 font-bold">
          Faça o cadastro e comece a usar!
        </Text>
      </header>
      {model ? (
        <BoxErro className="flex flex-col items-center justify-center mt-2 mb-4 rounded bg-red-500 h-8">
          <Text className="text-gray-800 text-center font-black text-sm p-1">
            Já existe usuário cadastrado com esse email!
          </Text>
        </BoxErro>
      ) : null}
      <form
        onSubmit={handleSubimit}
        className="flex flex-col items-stretch gap-4 w-full max-w-lg"
      >
        <Text size="sm" color="light-gray">
          Nome
        </Text>
        <CustomInput.Root>
          <CustomInput.Icon>
            <User />
          </CustomInput.Icon>
          <CustomInput.Input
            placeholder="Digite seu Nome"
            type="text"
            required
            id="name"
          />
        </CustomInput.Root>
        <Text size="sm" color="light-gray">
          Endereço de email
        </Text>
        <CustomInput.Root>
          <CustomInput.Icon>
            <Envelope />
          </CustomInput.Icon>
          <CustomInput.Input
            placeholder="Digite seu email"
            type="email"
            required
            id="email"
          />
        </CustomInput.Root>
        <Text size="sm" color="light-gray">
          Senha
        </Text>
        <CustomInput.Root>
          <CustomInput.Icon>
            <Lock />
          </CustomInput.Icon>
          <CustomInput.Input
            placeholder="*****"
            type="password"
            required
            id="password"
            minLength={5}
          />
        </CustomInput.Root>
        <CustomButton className="w-full py-3 px-4" type="submit">
          Cadastrar
        </CustomButton>
        <Text color="gray" className="text-sm text-center">
          <Link to={"/"} className="underline hover:text-gray-200 ">
            Já possui uma conta? Clique aqui e faça login!
          </Link>
        </Text>
      </form>
    </div>
  );
}
