import { api } from "./api"

export type TripDetails = {
  id: string
  destination: string
  starts_at: string
  ends_at: string
  is_confirmed: boolean
}

// uma nova tipagem reaproveitando TripDetails
// usando Omit<TypeReaproveitado, Campos que queremos omitir>
type TripCreate = Omit<TripDetails, "id" | "is_confirmed"> & {
  emails_to_invite: string[]
}

async function getById(id: string) {
  try {
    const { data } = await api.get<TripDetails>(`/trips/${id}`)
    return data
  } catch (error) {
    // lança o erro para frente
    throw error
  }
}

async function create({ destination, starts_at, ends_at, emails_to_invite }: TripCreate) {
  try {
    const { data } = await api.post<{ trip_id: string }>("/trips", {
      destination,
      starts_at,
      ends_at,
      emails_to_invite,
      // nesse caso, deixamos 2 campos necessários para criar uma viagem fixos
      owner_name: "Owner Name",
      owner_email: "owner@email.com"
    })

    return data
  } catch (error) {
    throw error
  }
}

async function update({ id, destination, starts_at, ends_at }: Omit<TripDetails, "is_confirmed">){
  try {
    await api.put(`/trips/${id}`, {
      destination,
      starts_at,
      ends_at
    })
  } catch (error) {
    throw error;
    
  }
}

export const tripServer = { getById, create, update }
