<?php
class Player_Model extends CI_Model
{

    public function get_name($id)
    {
        $this->db->where('id', $id);
        $this->db->limit(1);
        $query = $this->db->get('player');
        if ($query->num_rows() == 1) {
            $row = $query->row();
            return $row->user_name;
        }
        return FALSE;
    }

    public function get_all_names()
    {
        $this->db->order_by('user_name', 'asc');
        $query = $this->db->get('player');
        if ($query->num_rows() > 0) {
            foreach ($query->result() as $row) {
                $result[$row->id] = $row->user_name;
            }
            return $result;
        }
        return FALSE;
    }

}

?>