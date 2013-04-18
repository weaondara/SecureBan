<?php
class MY_Controller extends CI_Controller
{

    public $globaldata = array();

    public function __construct()
    {
        parent::__construct();
        $this->form_validation->set_error_delimiters('<div class="message message_error">', '</div>');

        if ($this->session->flashdata('message')) {
            if ($this->session->flashdata('message_type')) {
                $this->globaldata['message'] = $this->session->flashdata('message');
                $this->globaldata['message_type'] = $this->session->flashdata('message_type');
            } else {
                $this->globaldata['message'] = $this->session->flashdata('message');
                $this->globaldata['message_type'] = 'info';
            }
        }
    }

}

?>