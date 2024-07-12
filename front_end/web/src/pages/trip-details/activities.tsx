import { Plus, CircleCheck, CircleDashed } from "lucide-react"
import { Button } from "../../components/button"
import { useParams } from "react-router-dom"
import { useEffect, useState } from "react"
import { api } from "../../lib/axios"
import { Activity, ActivityListResponse } from "../../model/ActivityListResponse"
import { format } from "date-fns"
import { ptBR } from "date-fns/locale"

interface ActivitiesProps {
  onOpenActivityModal: () => void
}

/**
 * Componte relacionado a visualização e adição de atividades
 */
export function Activities({ onOpenActivityModal }: ActivitiesProps) {
  const { tripId } = useParams()
  const [activities, setActivities] = useState<Activity[]>([])

  function toDay(date: string) {
    return format(date, "d")
  }

  function toDayOfWeek(date: string) {
    return format(date, "EEEE", { locale: ptBR })
  }

  function toHour(date: string) {
    return format(date, "HH:mm", { locale: ptBR })
  }

  function checkIfDatePassed(date: string) {
    // The date you want to check
    const inputDate = new Date(date)

    // Get the current date
    const currentDate = new Date()

    // Compare the input date with the current date
    if (inputDate < currentDate) {
      return true
    } else {
      return false
    }
  }

  useEffect(() => {
    api
      .get<ActivityListResponse>(`/trips/${tripId}/activities`)
      .then(resp => setActivities(resp.data.activities))
  }, [tripId])

  return (
    <div className="flex-1 space-y-6">
      <div className="flex items-center justify-between">
        <h2 className="text-3xl font-semibold">Atividades</h2>
        <Button onClick={onOpenActivityModal}>
          <Plus className="size-5" />
          Cadastrar Atividade
        </Button>
      </div>

      <div className="space-y-8">
        {activities.map(category => {
          return (
            <div
              key={category.date}
              className="space-y-2.5">
              <div className="flex gap-2 items-baseline">
                <span className="text-xl text-zinc-300 font-semibold">
                  Dia {toDay(category.date)}
                </span>
                <span className="text-xs text-zinc-500">{toDayOfWeek(category.date)}</span>
              </div>

              {category.activities.length > 0 ? (
                <div className="space-y-2.5">
                  {category.activities.map(activity => {
                    return (
                      <div
                        key={activity.id}
                        className="space-y-2.5">
                        <div className="px-4 py-2.5 bg-zinc-900 rounded-xl shadow-shape flex items-center gap-3">
                          {checkIfDatePassed(activity.occurs_at) ? (
                            <CircleCheck className="size-5 text-line-300" />
                          ) : (
                            <CircleDashed className="size-5 text-line-300" />
                          )}

                          <span className="text-zinc-100">{activity.title}</span>
                          <span className="text-zinc-400 text-sm ml-auto">
                            {toHour(activity.occurs_at)}h
                          </span>
                        </div>
                      </div>
                    )
                  })}
                </div>
              ) : (
                <p className="text-zinc-500 text-sm">Nenhuma atividade cadastrada nessa data.</p>
              )}
            </div>
          )
        })}
      </div>
    </div>
  )
}
