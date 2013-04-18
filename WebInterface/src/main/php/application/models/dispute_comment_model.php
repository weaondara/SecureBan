<?php
class Dispute_Comment extends CI_Model
{

    public function getAllCommentsByBanID($banId)
    {

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