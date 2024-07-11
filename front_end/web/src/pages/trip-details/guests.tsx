import { CircleDashed, CircleCheck } from "lucide-react";

/**
 * Componente de visualização dos convidados para a viagem, mostrando se eles confirmaram ou não
 */
export function Guests() {
  return (
    <div className="space-y-5">
      <div className="flex items-center justify-between gap-4">
        <div className="space-y-1.5">
          <span className="block font-medium text-zinc-100">Jéssica White</span>
          <span className="block text-sm text-zinc-400 truncate">jessica.white@email.com</span>
        </div>
        <CircleDashed className="size-5 text-zinc-400" />
      </div>

      <div className="flex items-center justify-between gap-4">
        <div className="space-y-1.5">
          <span className="block font-medium text-zinc-100">Rita Maconha</span>
          <span className="block text-sm text-zinc-400 truncate">rita.maconha@email.com</span>
        </div>
        <CircleCheck className="size-5 text-zinc-400" />
      </div>
    </div>
  )
}
