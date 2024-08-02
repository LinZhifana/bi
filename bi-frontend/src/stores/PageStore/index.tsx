import AddchartIcon from '@mui/icons-material/Addchart'
import BarChartIcon from '@mui/icons-material/BarChart'
import { create } from 'zustand'

export const pageStore: pageEntry_t[] = [
  {
    name: 'Add chart',
    to: '/add_chart',
    icon: <AddchartIcon />,
  },
  {
    name: 'My chart',
    to: '/my_chart',
    icon: <BarChartIcon />,
  },
]

type pageEntry_t = {
  name: string
  to: string
  icon: JSX.Element
}

type ChartPageStore = {
  open: boolean
  drawerWidth: number
  pageName: string
  pageLink: string
  pageIcon: JSX.Element

  setOpen: (open: boolean) => void
  setPage: (pageEntry: pageEntry_t) => void
}

export const useChartPageStore = create<ChartPageStore>()((set) => ({
  open: false,
  drawerWidth: 240,
  pageName: 'Add chart',
  pageLink: '/add_chart',
  pageIcon: <AddchartIcon />,

  setOpen: (open: boolean) => set(() => ({ open: open })),
  setPage: (pageEntry: pageEntry_t) =>
    set({
      pageName: pageEntry.name,
      pageLink: pageEntry.to,
      pageIcon: pageEntry.icon,
    }),
}))
