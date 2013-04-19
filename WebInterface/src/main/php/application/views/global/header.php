<!DOCTYPE HTML>
<html>
<head>
    <title>SecureBan Web-Interface</title>
    <meta charset="utf-8"/>
    <link rel="stylesheet" href="<?php echo base_url(); ?>design/style.css" type="text/css" media="all"/>
    <script type="text/javascript" src="js/jquery.smooth-scroll.min.js"></script>
    <script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.8.18.custom.min.js"></script>
    <script type="text/javascript" src="js/lightbox.js"></script>
</head>
<body>
<div id="wrap">
    <div id="header">
        <div id="loginbox">
            <?php if ($this->user_model->is_logged_in()): ?>
                <p>Welcome, <?php echo $this->session->userdata('username'); ?>! <a
                        href="<?php echo site_url('user/logout'); ?>">Logout &raquo;</a></p>
            <?php else: ?>
                <p>You are not logged in! <a href="<?php echo site_url('user/login'); ?>">Login &raquo;</a></p>
            <?php endif; ?>
        </div>
        <h1>SecureBan Web-Interface</h1>

        <div style="clear: both;"></div>
    </div>
    <div id="navi">
        <div id="version">Version 1.1</div>
        <ul>
            <?php if ($this->user_model->is_logged_in()): ?>
                <li><a href="<?php echo site_url('ban'); ?>">Ban overview</a></li>
                <li><a href="<?php echo site_url('user/logout'); ?>">Logout</a></li>
            <?php else: ?>
                <li><a href="<?php echo site_url('ban'); ?>">Ban overview</a></li>
                <li><a href="<?php echo site_url('user/login'); ?>">Login</a></li>
            <?php endif; ?>
        </ul>
        <div style="clear: both;"></div>
    </div>
    <div id="content">
<?php
if (isset($message)) {
    echo '<div class="message message_' . $message_type . '">' . $message . '</div>';
}
?>