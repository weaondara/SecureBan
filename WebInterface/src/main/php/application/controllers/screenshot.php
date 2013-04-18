<?php
Class Screenshot extends MY_Controller
{

    function __construct()
    {
        parent::__construct();
        $this->load->model('screenshot_model');
    }

    public function upload($ban_id)
    {
        if ($this->user_model->is_admin()) {
            $config = array(
                'upload_path' => './screenshot/tmp/',
                'allowed_types' => 'gif|jpg|png',
                'max_size' => '4096'
            );
            $this->load->library('upload', $config);

            if ($this->upload->do_upload()) {
                $filename = $this->upload->data();
                $filename = $filename['file_name'];
                $this->screenshot_model->add($ban_id, $filename);
                $this->session->set_flashdata('message', 'Screenshot uploaded.');
                $this->session->set_flashdata('message_type', 'success');
            } else {
                $this->session->set_flashdata('message', $this->upload->display_errors());
                $this->session->set_flashdata('message_type', 'error');
            }
            redirect('ban/details/' . $ban_id);
        } else {
            show_404();
        }
    }

    public function delete($id, $ban_id)
    {
        if ($this->user_model->is_admin()) {
            $this->session->set_flashdata('message', 'Screenshot deleted.');
            $this->session->set_flashdata('message_type', 'success');
            $this->screenshot_model->delete($id);
            redirect('ban/details/' . $ban_id);
        } else {
            show_404();
        }
    }

}

?>