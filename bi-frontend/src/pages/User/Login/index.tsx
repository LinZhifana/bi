import { Alert, Button, Grid } from '@mui/material'
import Box from '@mui/material/Box'
import TextField from '@mui/material/TextField'
import { useLoginState } from '../../../stores/LoginStore'
import { UserLoginRequest, UserVO } from '../../../services/Api'
import { API } from '../../../services'
import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'

const alertState = {
  default: 2,
  success: 1,
  fail: 0,
}

const LoginPage: React.FC = () => {
  const navigate = useNavigate()
  const { login } = useLoginState()

  const [userInfo, setUserInfo] = useState({ username: '', password: '' })

  const [alert, setAlert] = useState(alertState.default)

  const handleLogin = async (req: UserLoginRequest) => {
    const res = await API.user.userLogin(req)
    const data = res.data
    if (data.code === 0) {
      setAlert(alertState.success)
      login(data.data as UserVO)
      navigate('/add_chart')
    } else if (data.code === 40000) {
      setAlert(alertState.fail)
    }
  }

  const showAlert = () => {
    if (alert === alertState.success)
      return <Alert severity="success">Login successfully.</Alert>
    if (alert === alertState.fail)
      return <Alert severity="error">Wrong username or password!</Alert>
    if (alert === alertState.default) return <></>
  }

  return (
    <>
      <Box component="form" noValidate autoComplete="off" maxWidth="sm">
        <Grid container spacing={2}>
          <Grid item xs={12}>
            <TextField
              id="username-input"
              label="Username"
              type="text"
              autoComplete="current-username"
              fullWidth
              value={userInfo.username}
              onChange={(e) => {
                setUserInfo({ ...userInfo, username: e.target.value })
                setAlert(alertState.default)
              }}
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              id="password-input"
              label="Password"
              type="password"
              autoComplete="current-password"
              fullWidth
              value={userInfo.password}
              onChange={(e) => {
                setUserInfo({ ...userInfo, password: e.target.value })
                setAlert(alertState.default)
              }}
            />
          </Grid>
          <Grid item xs={12}>
            <Link to={'/register'}>
              <Button variant="outlined" fullWidth>
                Register
              </Button>
            </Link>
          </Grid>
          <Grid item xs={12}>
            <Button
              variant="outlined"
              fullWidth
              onClick={() =>
                handleLogin({
                  userAccount: userInfo.username,
                  userPassword: userInfo.password,
                })
              }>
              Login
            </Button>
          </Grid>
        </Grid>
      </Box>

      <Box
        component="form"
        noValidate
        autoComplete="off"
        maxWidth="sm"
        marginTop={2}>
        {showAlert()}
      </Box>
    </>
  )
}

export default LoginPage
