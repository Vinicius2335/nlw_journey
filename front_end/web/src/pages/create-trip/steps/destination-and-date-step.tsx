import { ArrowRight, Calendar, MapPin, Settings2, X } from "lucide-react"
import { useState } from "react"
import { DateRange, DayPicker } from "react-day-picker"
import "react-day-picker/dist/style.css"
import { Button } from "../../../components/button"
import { format } from "date-fns"

interface DestinationAndDateStepProps {
  isGuestsInputOpen: boolean
  onCloseGuestsInput: () => void
  onOpenGuestsInput: () => void
  onSetDestination: (destination: string) => void
  eventStartAndEndDates:  DateRange | undefined
  onSetEventStartAndEndDates: (dates: DateRange | undefined) => void
}

/**
 * Componente onde o usuário irá inserir o destino e a data da viagem
 */
export function DestinationAndDateStep({
  isGuestsInputOpen,
  onCloseGuestsInput,
  onOpenGuestsInput,
  onSetDestination,
  eventStartAndEndDates,
  onSetEventStartAndEndDates
}: DestinationAndDateStepProps) {
  const [isDatePickerOpen, setIsDatePickerOpen] = useState(false)


  function openDatepicker() {
    setIsDatePickerOpen(true)
  }

  function closeDatepicker() {
    setIsDatePickerOpen(false)
  }

  const displayedDate =
    eventStartAndEndDates && eventStartAndEndDates.from && eventStartAndEndDates.to
      ? format(eventStartAndEndDates.from, "d ' de ' LLL").concat(' até ')
        .concat(format(eventStartAndEndDates.to, "d ' de ' LLL"))
      : null

  return (
    <div className="h-16 px-4 text-zinc-50 rounded-xl flex items-center shadow-shape gap-3">
      <div className="flex items-center gap-2 flex-1">
        <MapPin className="size-5 text-zinc-400" />
        <input
          type="text"
          placeholder="Para onde você vai?"
          className="bg-transparent text-lg placeholder-zinc-400 outline-none flex-1"
          disabled={isGuestsInputOpen}
          onChange={event => onSetDestination(event.target.value)}
        />
      </div>

      <button
        className="flex items-center gap-2 text-left w-60"
        disabled={isGuestsInputOpen}
        onClick={openDatepicker}>
        <Calendar className="size-5 text-zinc-400" />
        <span className="bg-transparent text-lg text-zinc-400 w-40 flex-1">
          {displayedDate || "Quando?"}
        </span>
      </button>

      <div className="w-px h-6 bg-zinc-800" />

      {isGuestsInputOpen ? (
        <Button
          variant="secondary"
          onClick={onCloseGuestsInput}>
          Alterar local/data
          <Settings2 className="size-5" />
        </Button>
      ) : (
        <Button onClick={onOpenGuestsInput}>
          Continuar
          <ArrowRight className="size-5" />
        </Button>
      )}

      {/* MODAL DATEPICKER */}
      {isDatePickerOpen && (
        <div className="fixed inset-0 bg-black/60 flex items-center justify-center">
          {/* 320px */}
          <div className="rounded-xl py-5 px-6 shadow-shape bg-zinc-900 space-y-5">
            <div className="space-y-2">
              <div className="flex item-center justify-between">
                <h2 className="text-lg font-semibold">Selecione a Data</h2>
                <button onClick={closeDatepicker}>
                  <X className="side-5 text-zinc-400" />
                </button>
              </div>
            </div>

            <DayPicker
              mode="range"
              selected={eventStartAndEndDates}
              onSelect={onSetEventStartAndEndDates}
              classNames={{
                day_selected:
                  "bg-lime-400 text-zinc-950 !hover:bg-lime-400 !hover:text-zinc-950 !focus:bg-lime-400 !focus:text-zinc-950",
                day_today: "text-lime-400 font-bold"
              }}
            />
          </div>
        </div>
      )}
    </div>
  )
}
