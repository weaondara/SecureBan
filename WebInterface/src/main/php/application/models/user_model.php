<?php
class User_Model extends CI_Model
{

    private $error_msg;

    public function login($username, $password)
    {
        $result = file_get_contents('https://login.minecraft.net?user=' . urlencode($username) . '&password=' . urlencode($password) . '&version=14');
        $logindata = explode(':', $result);
        if (count($logindata) == 5) {
            $version = $logindata[0];
            $user = $logindata[2];
            $sessionID = $logindata[3];
            $UUID = $logindata[4];
            $this->session->set_userdata('username', $user);
            return true;
        } else {
            $this->error_msg = $result;
            return false;
        }
    }

    public function logout()
    {
        $this->session->unset_userdata('username');
    }

    public function get_error_msg()
    {
        return $this->error_msg;
    }

    public function is_logged_in()
    {
        if ($this->session->userdata('username')) {
            return true;
        } else {
            return false;
        }
    }

    public function is_admin()
    {
        if ($this->is_logged_in() && in_array($this->session->userdata('username'), $this->config->item('admins'))) {
            return true;
        } else {
            return false;
        }
    }

}

?>