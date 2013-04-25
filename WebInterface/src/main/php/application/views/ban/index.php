<h2>Ban overview</h2>
<div style="float: right;">
    <?php echo form_open('ban/set_entries_per_page'); ?>
    <label for="entries_per_page">Entries per page:</label>
    <?php
    $data = array(
        10 => 10,
        25 => 25,
        50 => 50,
        100 => 100
    );
    echo form_dropdown('entries_per_page', $data, $entries_per_page, 'onchange="this.form.submit();"');
    ?>
    <noscript><input type="submit" value="Go"/></noscript>
    <?php echo form_close(); ?>
</div>
<?php echo validation_errors(); ?>
<?php echo form_open('ban/index'); ?>
<p><label for="player_id">Search by username:</label>
    <?php
    $data = array(
        'id' => 'player_id',
        'name' => 'player_id',
    );
    echo form_input($data);
    ?>
    <script type="text/javascript">
        $(function () {
            $('#player_id').autocomplete({source: [
                <?php
                $query = $this->db->get('player');
                if($query->num_rows()>=1){
                    foreach($query->result() as $row){
                        echo "\"".$row->user_name."\",";
                    }
                echo "\"all\"";
                }
             ?>
            ]});
        });
    </script>
<noscript><input type="submit" value="Go"/></noscript></p>
<?php echo form_close(); ?>
<?php if ($ban_list): ?>
    <p>Page: <?php echo $page_navi; ?></p>
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
    <p>Page: <?php echo $page_navi; ?></p>
<?php endif; ?>