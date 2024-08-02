import Box from '@mui/material/Box'
import CssBaseline from '@mui/material/CssBaseline'
import AppBarComp from '../../../components/AppBar'
import DrawerComp from '../../../components/Drawer'

const MyChartPage: React.FC = () => {
  return (
    <Box sx={{ display: 'flex' }}>
      <CssBaseline />
      <AppBarComp />
      <DrawerComp />
    </Box>
  )
}

export default MyChartPage
