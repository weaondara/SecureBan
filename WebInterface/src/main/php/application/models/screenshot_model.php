<?php
class Screenshot_Model extends CI_Model
{

    public function add($ban_id, $filename)
    {
        $data = array(
            'ban_id' => $ban_id,
            'staff_name' => $this->session->userdata('username'),
            'date' => time()
        );
        $this->db->insert('sbwi_screenshot', $data);
        $last_id = $this->db->insert_id();

        copy('./screenshot/tmp/' . $filename, './screenshot/' . $last_id . '.png');
        unlink('./screenshot/tmp/' . $filename);

        $config = array(
            'image_library' => 'gd2',
            'source_image' => './screenshot/' . $last_id . '.png',
            'create_thumb' => TRUE,
            'maintain_ratio' => TRUE,
            'width' => 150,
            'height' => 100
        );
        $this->load->library('image_lib', $config);
        $this->image_lib->resize();
    }

    public function delete($id)
    {
        $this->db->where('id', $id);
        $this->db->delete('sbwi_screenshot');
        unlink('./screenshot/' . $id . '.png');
        unlink('./screenshot/' . $id . '_thumb.png');
    }

    public function get_by_ban($ban_id)
    {
        $this->db->where('ban_id', $ban_id);
        $this->db->order_by('date', 'desc');
        $query = $this->db->get('sbwi_screenshot');
        if ($query->num_rows() > 0) {
            foreach ($query->result() as $row) {
                $result[] = $row->id;
            }
            return $result;
        }
        return false;
    }

}

?>