<h2>Login</h2>
<p>Please log in with your minecraft username and password.</p>
<?php if (!empty($error_msg)) {
    echo '<p>There was an error: ' . $error_msg . '</p>';
} ?>
<?php echo validation_errors(); ?>
<?php echo form_open(); ?>
<table>
    <tr>
        <td><label for="username">Username:</label></td>
        <td><input type="text" name="username" id="username"/></td>
    </tr>
    <tr>
        <td><label for="password">Password:</label></td>
        <td><input type="password" name="password" id="password"/></td>
    </tr>
</table>
<p><input type="submit" value="Login"/></p>
<?php echo form_close(); ?>