import { Alert, Box, Button, Grid, TextField } from '@mui/material'
import { useLoginState } from '../../../stores/LoginStore'
import { useState } from 'react'
import { UserRegisterRequest, UserVO } from '../../../services/Api'
import { API } from '../../../services'
import { Link, useNavigate } from 'react-router-dom'

const alertState = {
  default: 3,
  success: 2,
  password_error: 1,
  username_error: 0,
}

const RegisterPage: React.FC = () => {
  const navigate = useNavigate()
  const { login } = useLoginState()

  const [userInfo, setUserInfo] = useState({
    username: '',
    password: '',
    checkPassword: '',
  })

  const [alert, setAlert] = useState(alertState.default)

  const showAlert = () => {
    if (alert === alertState.success)
      return <Alert severity="success">Login successfully.</Alert>
    if (alert === alertState.username_error)
      return <Alert severity="error">Username is occupied!</Alert>
    if (alert === alertState.password_error)
      return (
        <Alert severity="error">The passwords you entered do not match!</Alert>
      )
    if (alert === alertState.default) return <></>
  }

  const handleRegister = async (req: UserRegisterRequest) => {
    if (req.userPassword != req.checkPassword) {
      setAlert(alertState.password_error)
      return
    }

    const res = await API.user.userRegister(req)
    const data = res.data
    if (data.code === 0) {
      setAlert(alertState.success)
      login(data.data as UserVO)
      navigate('/add_chart')
    } else if (data.code === 40000) {
      setAlert(alertState.username_error)
    }
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
            <TextField
              id="password-input"
              label="Check Password"
              type="password"
              autoComplete="current-password"
              fullWidth
              value={userInfo.checkPassword}
              onChange={(e) => {
                setUserInfo({ ...userInfo, checkPassword: e.target.value })
                setAlert(alertState.default)
              }}
            />
          </Grid>
          <Grid item xs={12}>
            <Link to="/login">
              <Button variant="outlined" fullWidth>
                Return
              </Button>
            </Link>
          </Grid>
          <Grid item xs={12}>
            <Button
              variant="outlined"
              fullWidth
              onClick={() =>
                handleRegister({
                  userAccount: userInfo.username,
                  userPassword: userInfo.password,
                  checkPassword: userInfo.checkPassword,
                })
              }>
              Confirm
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

export default RegisterPage
