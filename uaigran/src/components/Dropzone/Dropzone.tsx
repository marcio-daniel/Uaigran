import { useCallback, useState } from "react";
import { useDropzone } from "react-dropzone";
import Text from "../Text/Text";
import clsx from "clsx";

interface DropzoneProps{
    onFileUploaded: (file: File) => void;
    className?: string;
}

export function Dropzone({onFileUploaded,className} : DropzoneProps) {
  const [selectedFileUrl, setSelectedFileUrl] = useState("");

  const onDrop = useCallback((acceptedFiles: any[]) => {
    const file = acceptedFiles[0];
    const fileUrl = URL.createObjectURL(file);
    setSelectedFileUrl(fileUrl);
    onFileUploaded(file);
  },[onFileUploaded]);
  const {getRootProps,getInputProps,isDragActive} = useDropzone({
    onDrop,
  });
  return (
    <div className={
      clsx(
        "flex px-2 py-2 rounded-lg bg-[#202024] focus-within:ring-2 ring-[#E1E1E1] max-w-lg  justify-center",
        className,
      )
    } {...getRootProps()}>
      <input {...getInputProps()}/>
      {selectedFileUrl ? 
        (
            <img src={selectedFileUrl} className="h-[235px] rounded-lg"/>
        )
        :(
        <Text size="sm" color="light-gray">Arraste a imagem aqui, ou clique aqui e a selecione!</Text>
        )  
    }
    </div>
  );
}
