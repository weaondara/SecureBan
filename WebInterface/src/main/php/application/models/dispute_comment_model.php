<?php
class Dispute_Comment_Model extends CI_Model
{

    public function getAllCommentsByBanID($banId)
    {
        $this->db->where('ban_id', $banId);
        $this->db->from('sbwi_dispute_comment');
        $this->db->order_by('date', 'asc');
        $query = $this->db->get();
        if ($query->num_rows() >= 1) {
            $result = $this->handle_data($query->row());
            return $result;
        }
        return FALSE;
    }

    private function handle_data($row)
    {
        $result['date'] = $row->date;
        $result['player'] = $row->player;
        $result['comment'] = $row->comment;
        return $result;

    }

    public function addComment($banId, $comment)
    {
        $playerId = 0;

        $data = array(
            'ban_id' => $banId,
            'player_id' => $playerId,
            'date' => time(),
            'comment' => $comment
        );

        $this->db->insert('sbwi_dispute_comment', $data);
    }

}

?>