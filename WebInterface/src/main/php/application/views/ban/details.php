<h2>Ban details</h2>
<div style="float: right;">
    <table class="data" style="width: 138px; float: left; margin: 0px 20px;">
        <tr>
            <th>Avatar</th>
        </tr>
        <tr>
            <td><img src="https://minotar.net/helm/<?php echo $ban_data['player_name']; ?>/80.png" alt="" height="128"
                     width="128"/></td>
        </tr>
    </table>
    <table class="data" style="width: 240px;">
        <tr>
            <th colspan="2">General information</th>
        </tr>
        <tr>
            <td>Player name:</td>
            <td><?php echo $ban_data['player_name']; ?></td>
        </tr>
        <tr>
            <td>Banned by:</td>
            <td><?php echo $ban_data['staff_name']; ?></td>
        </tr>
        <tr>
            <td>Type:</td>
            <td><?php echo $ban_data['type']; ?></td>
        </tr>
        <tr>
            <td>Date:</td>
            <td><?php echo $ban_data['start']; ?></td>
        </tr>
        <tr>
            <td>Expires:</td>
            <td><?php echo $ban_data['expired']; ?></td>
        </tr>
        <tr>
            <td>Save state:</td>
            <td><?php echo $ban_data['save_state']; ?></td>
        </tr>
    </table>
</div>
<h3>Reason</h3>
<p><?php echo $ban_data['reason']; ?></p>
<h3>Screenshots</h3>
<?php if ($this->user_model->is_admin()): ?>
    <?php echo form_open_multipart('screenshot/upload/' . $ban_data['id']); ?>
    <p><input type="file" name="userfile"/> <span class="small">File type PNG, JPEG or GIF, max. 4MB</span><br/><input
            type="submit" value="Upload file"/></p>
    <?php echo form_close(); ?>
<?php endif; ?>
<?php if ($screenshots): ?>
    <?php foreach ($screenshots as $screenshot_id): ?>
        <table class="screenshot">
            <tr>
                <td class="image"><a href="<?php echo base_url(); ?>screenshot/<?php echo $screenshot_id; ?>.png">
                        <img src="<?php echo base_url(); ?>screenshot/<?php echo $screenshot_id; ?>_thumb.png"/>
                    </a></td>
            </tr>
            <?php if ($this->user_model->is_admin()): ?>
                <tr>
                    <td>
                        <a href="<?php echo site_url('screenshot/delete/' . $screenshot_id . '/' . $ban_data['id']); ?>">Delete</a>
                    </td>
                </tr>
            <?php endif; ?>
        </table>
    <?php endforeach; ?>
<?php else: ?>
    <p>No screenshots available.</p>
<?php endif; ?>
<div style="clear: both;"></div>
<h3>All bans for this user</h3>
<table class="data" style="width: 100%;">
    <tr>
        <th></th>
        <th>Player name</th>
        <th>Banned by</th>
        <th>Date</th>
        <th></th>
        <th>Type</th>
        <th></th>
    </tr>
    <?php foreach ($ban_list as $row): ?>
        <tr>
            <td><img src="https://minotar.net/helm/<?php echo $row['player_name']; ?>/24.png" alt="" height="24"
                     width="24"/></td>
            <td><?php echo $row['player_name']; ?></td>
            <td><?php echo $row['staff_name']; ?></td>
            <td><?php echo $row['start']; ?></td>
            <td><?php echo $row['indicator']; ?></td>
            <td><?php echo $row['type']; ?></td>
            <td><a href="<?php echo site_url('ban/details/' . $row['id']); ?>">View Details</a></td>
        </tr>
    <?php endforeach; ?>
</table>
<table class="data" style="width: 100%">
    <tr>
        <th>Date</th>
        <th>Player</th>
        <th>Comment</th>
    </tr>
    <?php foreach ($ban_comment as $row) ?>
    <tr>
        <td><?php echo $row['date'] ?></td>
        <td><?php echo $row['player'] ?></td>
        <td><?php echo $row['comment'] ?></td>
    </tr>
</table>