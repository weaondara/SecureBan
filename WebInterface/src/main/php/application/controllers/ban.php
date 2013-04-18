<?php
Class Ban extends MY_Controller
{

    function __construct()
    {
        parent::__construct();
        $this->load->model('ban_model');
    }

    public function index($player_id = 'all', $start = 0)
    {
        $this->load->model('player_model');
        $this->load->library('form_validation');
        $this->load->helper('form');
        $this->load->library('pagination');

        $this->form_validation->set_rules('player_id', 'Player name', '');

        if ($this->form_validation->run()) {
            redirect('ban/index/' . $this->input->post('player_id'));
        }

        $data['all_names'] = $this->player_model->get_all_names();
        $data['player_id'] = $player_id;

        if ($this->input->cookie('entries_per_page')) {
            $data['entries_per_page'] = $this->input->cookie('entries_per_page', TRUE);
        } else {
            $data['entries_per_page'] = 25;
        }
        $data['ban_list'] = $this->ban_model->get_list($player_id, $start, $data['entries_per_page']);

        // Generate Page-Navigation
        $config = array(
            'base_url' => site_url('ban/index/' . $player_id . '/'),
            'total_rows' => $this->ban_model->get_total($player_id),
            'per_page' => $data['entries_per_page'],
            'uri_segment' => 4
        );
        $this->pagination->initialize($config);
        $data['page_navi'] = $this->pagination->create_links();

        $this->load->view('global/header.php', $this->globaldata);
        $this->load->view('ban/index.php', $data);
        $this->load->view('global/footer.php');
    }

    public function set_entries_per_page()
    {
        $this->load->library('form_validation');

        $this->form_validation->set_rules('entries_per_page', 'Player name', 'required|integer');

        if ($this->form_validation->run()) {
            echo $this->input->set_cookie('entries_per_page', $this->input->post('entries_per_page'), 3600 * 24 * 365);
        }

        redirect('ban');
    }

    public function details($id)
    {
        $this->load->helper('form');
        $this->load->model('screenshot_model');

        $data['screenshots'] = $this->screenshot_model->get_by_ban($id);
        $data['ban_data'] = $this->ban_model->get_data($id);
        $data['ban_list'] = $this->ban_model->get_list($data['ban_data']['player_id']);

        $this->load->view('global/header.php', $this->globaldata);
        $this->load->view('ban/details.php', $data);
        $this->load->view('global/footer.php');
    }

}

?>