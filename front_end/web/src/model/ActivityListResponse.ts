export interface ActivityListResponse {
  activities: Activity[]
}

export interface Activity {
  date : string
  activities: ActivityDetails[]
}

export interface ActivityDetails {
  id: string
  title: string
  occurs_at: string
}