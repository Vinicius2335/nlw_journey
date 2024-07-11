import { ArrowRight, UserRoundPlus } from "lucide-react"
import { Button } from "../../../components/button"

interface InviteGuestsStepProps {
  onOpenGuestsModal: () => void
  onOpenConfirmTripModal: () => void
  emailsToInvite: string[]
}

/**
 * Componente responsável por abrir modal que convida participantes e
 *  abrir o modal de confirmação de viagem
 */
export function InviteGuestsStep({
  onOpenGuestsModal,
  onOpenConfirmTripModal,
  emailsToInvite
}: InviteGuestsStepProps) {
  return (
    <div className="h-16 px-4 text-zinc-50 rounded-xl flex items-center shadow-shape gap-3">
      <button
        type="button"
        className="flex items-center gap-2 flex-1 text-left"
        onClick={onOpenGuestsModal}>
        <UserRoundPlus className="size-5 text-zinc-400" />
        {emailsToInvite.length > 0 ? (
          <span className="text-lg text-zinc-100 flex-1">
            {emailsToInvite.length} pessoa(s) convidada(s)
          </span>
        ) : (
          <span className="text-lg text-zinc-400 flex-1">Quem estará na viagem?</span>
        )}
      </button>

      <div className="w-px h-6 bg-zinc-800" />

      <Button onClick={onOpenConfirmTripModal}>
        <ArrowRight className="size-5" />
        Confirmar Viagem
      </Button>
    </div>
  )
}
