export interface Trip {
  id: string
  destination: string
  starts_at: string
  ends_at: string
  owner_email: string
  owner_name: string
  confirmed: boolean
}

export interface TripRequest {
  destination: string
  starts_at: string
  ends_at: string
  emails_to_invite: string[]
  owner_name: string
  ownerEmail: string
}
