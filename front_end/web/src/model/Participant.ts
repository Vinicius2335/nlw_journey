export interface ParticipantListResponse{
  participants: Participant[]
}

export interface Participant {
  id: string
  name: string
  email: string
  is_confirmed: boolean
}