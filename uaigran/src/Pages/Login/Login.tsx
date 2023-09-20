import Text from "../../components/Text/Text";
import { Envelope, Lock } from "@phosphor-icons/react";
import { CustomInput } from "../../components/TextInput/CustomInput";
import CustomButton from "../../components/CustomButton/CustomButton";
import { Link, useNavigate } from "react-router-dom";
import { FormEvent, useState } from "react";
import AuthFormElement from "../../Types/FormTypes/AuthFormElement";
import Auth from "../../Types/Auth";
import api from "../../Services/api";
import BoxErro from "../../components/BoxErro/BoxErro";

export default function Login() {
  const [model, setModel] = useState(false);
  const navigate = useNavigate();

  async function handleLogin(auth: Auth) {
    try {
      const response = await api.post("/authenticate", auth);
      const user = {
        user_id : response.data.user_id,
        acessToken: response.data.token
      }
      localStorage.setItem("user_id",user.user_id);
      localStorage.setItem("token",user.acessToken)
      navigate("/home");
    } catch (error) {
      if (error.response.status == 401) {
        setModel(true);
      }
    }
  }

  function handleSubmit(events: FormEvent<AuthFormElement>) {
    events.preventDefault();
    if (model) {
      setModel(false);
    }
    const form = events.currentTarget;
    const auth: Auth = {
      email: form.elements.email.value,
      password: form.elements.password.value,
    };
    handleLogin(auth);
  }
  return (
    <div className="flex flex-col items items-center mt-16">
      <header className="flex flex-col items items-center mt-14 mb-8">
        <h1 className="text-6xl text-center text-[#E1E1E1] font-bold font-sans">
          Uaigran
        </h1>
        <Text size="sm" color="gray" className="mt-1 opacity-50">
          Faça login e comece a usar!
        </Text>
      </header>
      {model ? (
        <BoxErro
          className="flex flex-col items-center justify-center mt-2 mb-4 rounded bg-red-500 h-8"
        >
          <Text className="text-gray-800 text-center font-black text-sm p-1">
          Email ou senha informados estão incorretos!
          </Text>
        </BoxErro>
      ) : null}
      <form
        onSubmit={handleSubmit}
        className="flex flex-col items-stretch gap-4 w-full max-w-sm"
      >
        <Text size="sm" color="light-gray">
          Endereço de e-mail
        </Text>
        <CustomInput.Root>
          <CustomInput.Icon>
            <Envelope />
          </CustomInput.Icon>
          <CustomInput.Input
            placeholder="Digite seu e-mail!"
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
          />
        </CustomInput.Root>
        <CustomButton type="submit" className="w-full py-3 px-4">Entrar</CustomButton>
        <Text color="gray" className="text-sm text-center">
          <Link to={"/registration"} className="underline hover:text-gray-200 ">
            Não possui uma conta? Clique aqui e faça o cadastro!
          </Link>
        </Text>
      </form>
    </div>
  );
}
