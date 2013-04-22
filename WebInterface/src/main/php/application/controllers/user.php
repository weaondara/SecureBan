<?php
Class User extends MY_Controller
{

    function __construct()
    {
        parent::__construct();
    }

    public function login()
    {
        $this->load->library('form_validation');
        $this->load->helper('form');

        $this->form_validation->set_rules('username', 'Username', 'required');
        $this->form_validation->set_rules('password', 'password', 'required');

        $data['error_msg'] = '';

        if ($this->form_validation->run()) {
            if ($this->input->post('username') == '_admin_') {
                $this->db->where('property', 'adminpass');
                $query = $this->db->get('sbwi_configuration');
                if ($query->num_rows() == 1) {
                    $row = $query->row();
                    if ($row->propertyvalue == $this->input->post('password')) {
                        $this->session->set_flashdata('message', 'Login successful.');
                        $this->session->set_flashdata('message_type', 'success');
                        $this->session->set_userdata('username', $this->input->post('username'));
                        redirect('ban');
                    } else {
                        $this->session->set_flashdata('message', $this->user_model->get_error_msg());
                        $this->session->set_flashdata('message_type', 'error');
                        redirect('user/login');
                    }
                }
            } else {
                if ($this->user_model->login($this->input->post('username'), $this->input->post('password'))) {

                    // Login successfull
                    $this->session->set_flashdata('message', 'Login successful.');
                    $this->session->set_flashdata('message_type', 'success');
                    redirect('ban');

                } else {

                    // Login failed
                    $this->session->set_flashdata('message', $this->user_model->get_error_msg());
                    $this->session->set_flashdata('message_type', 'error');
                    redirect('user/login');

                }
            }
        }

        $this->load->view('global/header.php', $this->globaldata);
        $this->load->view('user/login.php', $data);
        $this->load->view('global/footer.php');

    }

    public function logout()
    {
        $this->session->set_flashdata('message', 'Logout successful.');
        $this->session->set_flashdata('message_type', 'success');
        $this->user_model->logout();
        redirect('ban');
    }

}

?>