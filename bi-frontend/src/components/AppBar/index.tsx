import { styled } from '@mui/material/styles'
import MuiAppBar, { AppBarProps as MuiAppBarProps } from '@mui/material/AppBar'
import Toolbar from '@mui/material/Toolbar'
import IconButton from '@mui/material/IconButton'
import MenuIcon from '@mui/icons-material/Menu'
import Typography from '@mui/material/Typography'
import { useChartPageStore } from '../../stores/PageStore'
import React from 'react'

interface AppBarProps extends MuiAppBarProps {
  open?: boolean
}

const AppBar = styled(MuiAppBar, {
  shouldForwardProp: (prop) => prop !== 'open',
})<AppBarProps>(({ theme, open }) => {
  const { drawerWidth } = useChartPageStore()

  return {
    transition: theme.transitions.create(['margin', 'width'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
    ...(open && {
      width: `calc(100% - ${drawerWidth}px)`,
      marginLeft: `${drawerWidth}px`,
      transition: theme.transitions.create(['margin', 'width'], {
        easing: theme.transitions.easing.easeOut,
        duration: theme.transitions.duration.enteringScreen,
      }),
    }),
  }
})

const AppBarComp: React.FC = () => {
  const { open, setOpen, pageName } = useChartPageStore()

  return (
    <AppBar
      position="fixed"
      open={open}
      sx={{
        backgroundColor: '#BEC6A0',
      }}>
      <Toolbar>
        <IconButton
          color="inherit"
          aria-label="open drawer"
          onClick={() => setOpen(true)}
          edge="start"
          sx={{ mr: 2, ...(open && { display: 'none' }) }}>
          <MenuIcon />
        </IconButton>
        <Typography variant="h6" noWrap component="div">
          {pageName}
        </Typography>
      </Toolbar>
    </AppBar>
  )
}

export default AppBarComp
