import { format } from "date-fns"
import { Calendar, MapPin, X } from "lucide-react"
import { FormEvent, useEffect, useState } from "react"
import { DateRange, DayPicker } from "react-day-picker"
import { CustomModal } from "../../components/custom-modal"
import { CustomInput } from "../../components/custom-input"
import { toast } from "react-toastify"
import { useNavigate } from "react-router-dom"
import { api } from "../../lib/axios"
import { Trip } from "../../model/Trip"

interface UpdateTripModalProps {
  onCloseModal: () => void
  trip: Trip | undefined
}

/**
 * Modal responsável por atualizar viagem
 */
export function UpdateTripModal({ onCloseModal, trip }: UpdateTripModalProps) {
  const [isDatePickerOpen, setIsDatePickerOpen] = useState(false)
  const [eventStartAndEndDates, setEventStartAndEndDates] = useState<DateRange | undefined>()
  const [destination, setDestination] = useState("")

  const navigate = useNavigate()

  function openDatepicker() {
    setIsDatePickerOpen(true)
  }

  function closeDatepicker() {
    setIsDatePickerOpen(false)
  }

  async function updateTrip(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()

    const data = new FormData(event.currentTarget)
    const destination = data.get("destination")?.toString()

    if (!destination) {
      return toast.error("Campo de destino obrigatório.")
    }
    if (!eventStartAndEndDates?.from || !eventStartAndEndDates?.to) {
      return toast.error("Campo de data e hora são obrigatórios.")
    }

    await api.put(`/trips/${trip?.id}`, {
      destination,
      starts_at: eventStartAndEndDates.from.toISOString(),
      ends_at: eventStartAndEndDates.to.toISOString()
    })

    navigate(0)
  }

  const displayedDate =
    eventStartAndEndDates && eventStartAndEndDates.from && eventStartAndEndDates.to
      ? format(eventStartAndEndDates.from, "d ' de ' LLL")
          .concat(" até ")
          .concat(format(eventStartAndEndDates.to, "d ' de ' LLL"))
      : null

  useEffect(() => {
    if(trip === undefined){
      return
    }

    setDestination(trip.destination)

    setEventStartAndEndDates({
      from: new Date(trip.starts_at),
      to: new Date(trip.ends_at)
    })

  }, [trip])

  return (
    <CustomModal
      onCloseModal={onCloseModal}
      onClickButton={updateTrip}
      title={"Atualizar Viagem"}
      describe={""}
      btnDescribe={"Salvar"}>
      <CustomInput
        icon={MapPin}
        type="text"
        value={destination}
        onChange={e => setDestination(e.target.value)}
        placeholder={"Para onde você vai?"}
        name={"destination"}
      />
      <button
        type="button"
        className="flex items-center gap-2 text-left w-full h-14 px-4 bg-zinc-950 border border-zinc-800 rounded-lg"
        onClick={openDatepicker}>
        <Calendar className="size-5 text-zinc-400" />
        <span className="bg-transparent text-lg text-zinc-400 w-40 flex-1">
          {displayedDate || "Quando?"}
        </span>
      </button>
      {isDatePickerOpen && (
        <div className="fixed inset-0 bg-black/60 flex items-center justify-center">
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
              onSelect={setEventStartAndEndDates}
              classNames={{
                day_selected:
                  "bg-lime-400 text-zinc-950 !hover:bg-lime-400 !hover:text-zinc-950 !focus:bg-lime-400 !focus:text-zinc-950",
                day_today: "text-lime-400 font-bold"
              }}
            />
          </div>
        </div>
      )}
    </CustomModal>
  )
}
