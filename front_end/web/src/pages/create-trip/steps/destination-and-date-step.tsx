import { ArrowRight, Calendar, MapPin, Settings2 } from "lucide-react"
import { Button } from "../../../components/button"

interface DestinationAndDateStepProps {
  isGuestsInputOpen: boolean
  onCloseGuestsInput: () => void
  onOpenGuestsInput: () => void
}

/**
 * Componente onde o usuário irá inserir o destino e a data da viagem
 */
export function DestinationAndDateStep({
  isGuestsInputOpen,
  onCloseGuestsInput,
  onOpenGuestsInput
}: DestinationAndDateStepProps) {
  return (
    <div className="h-16 px-4 text-zinc-50 rounded-xl flex items-center shadow-shape gap-3">
      <div className="flex items-center gap-2 flex-1">
        <MapPin className="size-5 text-zinc-400" />
        <input
          type="text"
          placeholder="Para onde você vai?"
          className="bg-transparent text-lg placeholder-zinc-400 outline-none flex-1"
          disabled={isGuestsInputOpen}
        />
      </div>

      <div className="flex items-center gap-2">
        <Calendar className="size-5 text-zinc-400" />
        <input
          type="text"
          placeholder="Quando?"
          className="bg-transparent text-lg placeholder-zinc-400 w-40 outline-none"
          disabled={isGuestsInputOpen}
        />
      </div>

      <div className="w-px h-6 bg-zinc-800" />

      {isGuestsInputOpen ? (
        <Button variant="secondary" onClick={onCloseGuestsInput}>
          Alterar local/data
          <Settings2 className="size-5" />
        </Button>
      ) : (
        <Button onClick={onOpenGuestsInput}>
          Continuar
          <ArrowRight className="size-5" />
        </Button>
      )}
    </div>
  )
}
