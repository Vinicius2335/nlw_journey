import { Mail, User, X } from "lucide-react"
import { FormEvent } from "react"
import { Button } from "../../components/button"

interface ConfirmTripModalProps {
  onCloseConfirmTripModal: () => void
  onCreateTrip: (event: FormEvent<HTMLFormElement>) => void
  onSetOwnerName: (name: string) => void
  onSetOwnerEmail: (email: string) => void
}

/**
 * Modal responsável pela confirmação da viagem
 */
export function ConfirmTripModal({
  onCloseConfirmTripModal,
  onCreateTrip,
  onSetOwnerName,
  onSetOwnerEmail
}: ConfirmTripModalProps) {
  return (
    <div className="fixed inset-0 bg-black/60 flex items-center justify-center">
      <div className="w-[540px] rounded-xl py-5 px-6 shadow-shape bg-zinc-900 space-y-5">
        <div className="space-y-2">
          <div className="flex item-center justify-between">
            <h2 className="text-lg font-semibold">Confirmar Criação de Viagem</h2>
            <button onClick={onCloseConfirmTripModal}>
              <X className="side-5 text-zinc-400" />
            </button>
          </div>

          <p className="text-sm text-zinc-400">
            Para concluir a criação da viagem para{" "}
            <span className="font-semibold text-zinc-100">Florianopolis, Brasil</span> nas datas de{" "}
            <span className="font-semibold text-zinc-100"> 16 a 17 de agosto de 2024</span>,
            preencha seus dados abaixo:
          </p>
        </div>

        <form
          onSubmit={onCreateTrip}
          className="space-y-3">
          <div className="h-14 px-4 bg-zinc-950 border border-zinc-800 rounded-lg flex items-center gap-2">
            <User className="text-zinc-400 size-5" />
            <input
              type="text"
              name="name"
              className="bg-transparent text-lg placeholder-zinc-400 w-40 outline-none flex-1"
              placeholder="Seu nome completo"
              onChange={event => onSetOwnerName(event.target.value)}
            />
          </div>

          <div className="h-14 px-4 bg-zinc-950 border border-zinc-800 rounded-lg flex items-center gap-2">
            <Mail className="text-zinc-400 size-5" />
            <input
              type="email"
              name="email"
              className="bg-transparent text-lg placeholder-zinc-400 w-40 outline-none flex-1"
              placeholder="Seu e-mail pessoal"
              onChange={event => onSetOwnerEmail(event.target.value)}
            />
          </div>

          <Button
            type="submit"
            size="full">
            Confirmar criação de viagem
          </Button>
        </form>
      </div>
    </div>
  )
}
