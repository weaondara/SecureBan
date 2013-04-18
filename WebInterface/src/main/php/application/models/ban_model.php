<?php
class Ban_Model extends CI_Model
{

    public function get_total($player_id)
    {
        if ($player_id != 'all') {
            $this->db->where('player_id', $player_id);
        }
        $this->db->select('id');
        $query = $this->db->get('player_ban');
        return $query->num_rows();
    }

    public function get_data($id)
    {
        $this->db->where('id', $id);
        $query = $this->db->get('player_ban');
        if ($query->num_rows() == 1) {
            $result = $this->handle_data($query->row());
            return $result;
        }
        return FALSE;
    }

    public function get_list($player_id, $start = 0, $limit = 0)
    {
        if ($player_id != 'all') {
            $this->db->where('player_id', $player_id);
        }
        $this->db->order_by('start', 'desc');
        if ($start > 0 || $limit > 0) {
            $this->db->limit($limit, $start);
        }
        $query = $this->db->get('player_ban');
        if ($query->num_rows() > 0) {
            foreach ($query->result() as $row) {
                $result[$row->id] = $this->handle_data($row);
            }
            return $result;
        }
        return FALSE;
    }

    private function get_dispute_comment($banId)
    {

    }

    private function handle_data($row)
    {
        $this->load->model('player_model');
        $this->load->model('dispute_comment_model');

        $result['id'] = $row->id;
        $result['player_id'] = $row->player_id;
        $result['player_name'] = $this->player_model->get_name($row->player_id);
        $result['comment'] = $this->dispute_comment_model->getAllCommentsByBanID($row->id);
        $result['staff_name'] = $row->staff_name;
        if ($row->expired != 0 && $row->expired / 1000 < time()) {
            $result['type'] = 'Expired';
            $color = '#00FF00;';
        } else {
            switch ($row->ban_type) {
                case 'GLOBAL':
                    $result['type'] = 'Global ban';
                    $color = '#FF0000;';
                    break;
                case 'LOCAL':
                    $result['type'] = 'Local ban';
                    $color = '#FFBF00;';
                    break;
                case 'TEMP':
                    $result['type'] = 'Temporary ban';
                    $color = '#0040FF;';
                    break;
                default:
                    $result['type'] = $row->ban_type;
                    $color = '#666666;';
                    break;
            }
        }
        $result['indicator'] = '<div style="height: 10px; width: 10px; background-color: ' . $color . '; border-radius: 5px;"></div>';
        switch ($row->save_state) {
            case 'SAVED':
                $result['save_state'] = 'Saved';
                break;
            case 'QUEUE':
                $result['save_state'] = 'Queue';
                break;
            default:
                $result['save_state'] = $row->save_state;
                break;
        }
        $result['start'] = date($this->config->item('date_time_format'), $row->start / 1000);
        if (empty($row->expired)) {
            $result['expired'] = 'Never';
        } else {
            $result['expired'] = date($this->config->item('date_time_format'), $row->expired / 1000);
        }
        $result['reason'] = $row->ban_reason;
        return $result;
    }

}

?>