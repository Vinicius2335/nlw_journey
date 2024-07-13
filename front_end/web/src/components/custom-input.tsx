import { LucideProps } from "lucide-react";
import { Icon } from "./icon";
import { ComponentProps } from "react";

interface CustomInputProps extends ComponentProps<"input"> {
  icon: React.FC<LucideProps>
  placeholder: string
  name: string
}

export function CustomInput({ icon, name, placeholder, ...props }: CustomInputProps) {
  return (
    <div className="h-14 px-4 bg-zinc-950 border border-zinc-800 rounded-lg flex items-center gap-2">
      <Icon icon={icon}/>
      <input
        {...props}      
        name={name}
        className="bg-transparent text-lg placeholder-zinc-400 w-40 outline-none flex-1 [color-scheme:dark]"
        placeholder={placeholder}
      />
    </div>
  )
}
