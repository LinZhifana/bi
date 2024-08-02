import { useChartPageStore } from '../../stores/PageStore'
import { styled } from '@mui/material/styles'

const Main = styled('main', { shouldForwardProp: (prop) => prop !== 'open' })<{
  open?: boolean
}>(({ theme, open = false }) => {
  const { drawerWidth } = useChartPageStore()
  return {
    flexGrow: 1,
    padding: theme.spacing(3),
    transition: theme.transitions.create('margin', {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
    marginLeft: `-${drawerWidth}px`,
    ...(open && {
      transition: theme.transitions.create('margin', {
        easing: theme.transitions.easing.easeOut,
        duration: theme.transitions.duration.enteringScreen,
      }),
      marginLeft: 0,
    }),
  }
})

interface MainCompProps {
  children: React.ReactNode
}

const MainComp: React.FC<MainCompProps> = ({ children }) => {
  const { open } = useChartPageStore()
  return <Main open={open}>{children}</Main>
}

export default MainComp
