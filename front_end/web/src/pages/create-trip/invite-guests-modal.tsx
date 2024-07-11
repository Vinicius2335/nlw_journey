import { AtSign, Plus, X } from "lucide-react"
import { FormEvent } from "react"
import { Button } from "../../components/button"

interface InviteGuestsModalProps {
  onCloseGuestsModal: () => void
  emailsToInvite: string[]
  onRemoveEmailFromInvites: (email: string) => void
  onAddNewEmailToInvite: (event: FormEvent<HTMLFormElement>) => void
}

/**
 * Modal responsável por adicionar os participantes para a viagem
 */
export function InviteGuestsModal({
  onCloseGuestsModal,
  emailsToInvite,
  onRemoveEmailFromInvites,
  onAddNewEmailToInvite
}: InviteGuestsModalProps) {
  return (
    <div className="fixed inset-0 bg-black/60 flex items-center justify-center">
      <div className="w-[640px] rounded-xl py-5 px-6 shadow-shape bg-zinc-900 space-y-5">
        <div className="space-y-2">
          <div className="flex item-center justify-between">
            <h2 className="text-lg font-semibold">Selecionar Convidados</h2>
            <button onClick={onCloseGuestsModal}>
              <X className="side-5 text-zinc-400" />
            </button>
          </div>

          <p className="text-sm text-zinc-400">
            Os convidados irão receber e-mail para confirmar a participação na viagem.
          </p>
        </div>

        <div className="flex flex-wrap gap-2">
          {emailsToInvite.map(email => {
            return (
              <div
                key={email}
                className="py-1.5 px-2.5 rounded-md bg-zinc-800 flex items-center gap-2">
                <span className="text-zinc-300">{email}</span>
                <button type="button">
                  <X
                    className="size-4 text-zinc-400"
                    onClick={() => onRemoveEmailFromInvites(email)}
                  />
                </button>
              </div>
            )
          })}
        </div>

        <div className="w-full h-px bg-zinc-400" />

        <form
          onSubmit={onAddNewEmailToInvite}
          className="p-2.5 bg-zinc-950 border border-zinc-800 rounded-lg flex items-center gap-2">
          <AtSign className="text-zinc-400 size-5" />
          <input
            type="email"
            name="email"
            className="bg-transparent text-lg placeholder-zinc-400 w-40 outline-none flex-1"
            placeholder="Digite o e-mail do convidado"
          />

          <Button type="submit">
            <Plus className="size-5" />
            Convidar
          </Button>
        </form>
      </div>
    </div>
  )
}
